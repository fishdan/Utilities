<?php
/**
 * Plugin Name: WebPing
 * Description: A simple web-accessible ping endpoint. Visitors submit an authcode and phone number; if the authcode matches the admin-configured value, an email notification is sent.
 * Version: 1.0
 * Author: Dan Fishman
 */

if (!defined('ABSPATH')) {
    exit;
}

define('WEBPING_PLUGIN_DIR', plugin_dir_path(__FILE__));

// ---------------------------------------------------------------------------
// Activation: create the /webping page with our shortcode
// ---------------------------------------------------------------------------
register_activation_hook(__FILE__, 'webping_activate');

function webping_activate() {
    $slug = 'webping';
    $existing = get_page_by_path($slug);

    if (!$existing) {
        $page_id = wp_insert_post([
            'post_title'   => 'WebPing',
            'post_name'    => $slug,
            'post_content' => '[webping]',
            'post_status'  => 'publish',
            'post_type'    => 'page',
        ]);

        if (!is_wp_error($page_id)) {
            update_option('webping_page_id', $page_id);
        }
    } else {
        update_option('webping_page_id', $existing->ID);
    }
}

// ---------------------------------------------------------------------------
// Deactivation: optionally clean up the page
// ---------------------------------------------------------------------------
register_deactivation_hook(__FILE__, 'webping_deactivate');

function webping_deactivate() {
    $page_id = get_option('webping_page_id');
    if ($page_id) {
        wp_trash_post($page_id);
        delete_option('webping_page_id');
    }
}

// ---------------------------------------------------------------------------
// Admin settings page
// ---------------------------------------------------------------------------
add_action('admin_menu', 'webping_admin_menu');
add_action('admin_init', 'webping_register_settings');

function webping_admin_menu() {
    add_options_page(
        'WebPing Settings',
        'WebPing',
        'manage_options',
        'webping-settings',
        'webping_render_settings_page'
    );
}

function webping_register_settings() {
    register_setting('webping_settings_group', 'webping_authcode', [
        'type'              => 'string',
        'sanitize_callback' => 'sanitize_text_field',
        'default'           => '',
    ]);

    register_setting('webping_settings_group', 'webping_email', [
        'type'              => 'string',
        'sanitize_callback' => 'sanitize_email',
        'default'           => '',
    ]);

    add_settings_section(
        'webping_main_section',
        'WebPing Configuration',
        function () {
            echo '<p>Configure the authcode and notification email for WebPing.</p>';
        },
        'webping-settings'
    );

    add_settings_field(
        'webping_authcode',
        'Auth Code',
        'webping_render_authcode_field',
        'webping-settings',
        'webping_main_section'
    );

    add_settings_field(
        'webping_email',
        'Notification Email',
        'webping_render_email_field',
        'webping-settings',
        'webping_main_section'
    );
}

function webping_render_authcode_field() {
    $value = get_option('webping_authcode', '');
    echo '<input type="text" name="webping_authcode" value="' . esc_attr($value) . '" class="regular-text" />';
    echo '<p class="description">The auth code that must be submitted to trigger a notification.</p>';
}

function webping_render_email_field() {
    $value = get_option('webping_email', '');
    echo '<input type="email" name="webping_email" value="' . esc_attr($value) . '" class="regular-text" />';
    echo '<p class="description">The email address that will receive ping notifications.</p>';
}

function webping_render_settings_page() {
    if (!current_user_can('manage_options')) {
        wp_die('Unauthorized');
    }

    $page_id = get_option('webping_page_id');
    $page_url = $page_id ? get_permalink($page_id) : home_url('/webping');
    ?>
    <div class="wrap">
        <h1>WebPing Settings</h1>
        <form method="POST" action="options.php">
            <?php
            settings_fields('webping_settings_group');
            do_settings_sections('webping-settings');
            submit_button();
            ?>
        </form>
        <hr>
        <h2>Usage</h2>
        <p>Public form URL: <a href="<?php echo esc_url($page_url); ?>"><?php echo esc_html($page_url); ?></a></p>
        <p>GET request example:</p>
        <code><?php echo esc_html($page_url . '?authcode=YOUR_CODE&phone=5551234567'); ?></code>
    </div>
    <?php
}

// ---------------------------------------------------------------------------
// Front-end shortcode: [webping]
// ---------------------------------------------------------------------------
add_shortcode('webping', 'webping_shortcode');

function webping_shortcode() {
    // Check for submission via GET or POST
    $authcode = '';
    $phone    = '';
    $submitted = false;

    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['webping_submit'])) {
        $authcode  = sanitize_text_field($_POST['authcode'] ?? '');
        $phone     = sanitize_text_field($_POST['phone'] ?? '');
        $submitted = true;
    } elseif (isset($_GET['authcode']) && isset($_GET['phone'])) {
        $authcode  = sanitize_text_field($_GET['authcode']);
        $phone     = sanitize_text_field($_GET['phone']);
        $submitted = true;
    }

    if ($submitted) {
        webping_process_submission($authcode, $phone);
        return webping_render_confirmation();
    }

    return webping_render_form();
}

/**
 * Process the submission: if authcode matches, send the notification email.
 */
function webping_process_submission($authcode, $phone) {
    $stored_authcode = get_option('webping_authcode', '');
    $stored_email    = get_option('webping_email', '');

    if ($stored_authcode === '' || $stored_email === '') {
        return;
    }

    if ($authcode === $stored_authcode) {
        $subject = 'webping';
        $body    = esc_html($phone) . ' accessed your webping';

        wp_mail($stored_email, $subject, $body);
    }
}

/**
 * Render the submission form.
 */
function webping_render_form() {
    $page_id  = get_option('webping_page_id');
    $form_url = $page_id ? get_permalink($page_id) : home_url('/webping');

    ob_start();
    ?>
    <div style="max-width: 420px; margin: 40px auto; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;">
        <h2 style="text-align: center; margin-bottom: 24px;">WebPing</h2>
        <form method="POST" action="<?php echo esc_url($form_url); ?>"
              style="background: #fff; padding: 28px; border: 1px solid #ddd; border-radius: 8px; box-shadow: 0 2px 6px rgba(0,0,0,0.06);">
            <div style="margin-bottom: 16px;">
                <label for="webping-authcode" style="display: block; font-weight: 600; margin-bottom: 6px;">Auth Code</label>
                <input type="text" id="webping-authcode" name="authcode" required
                       style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;" />
            </div>
            <div style="margin-bottom: 20px;">
                <label for="webping-phone" style="display: block; font-weight: 600; margin-bottom: 6px;">Phone Number</label>
                <input type="tel" id="webping-phone" name="phone" required
                       style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;" />
            </div>
            <input type="hidden" name="webping_submit" value="1" />
            <button type="submit"
                    style="width: 100%; padding: 12px; background: #0073aa; color: #fff; border: none; border-radius: 4px; font-size: 16px; cursor: pointer;">
                Submit
            </button>
        </form>
    </div>
    <?php
    return ob_get_clean();
}

/**
 * Render the generic confirmation message (shown regardless of authcode validity).
 */
function webping_render_confirmation() {
    ob_start();
    ?>
    <div style="max-width: 420px; margin: 40px auto; text-align: center; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;">
        <div style="background: #fff; padding: 40px 28px; border: 1px solid #ddd; border-radius: 8px; box-shadow: 0 2px 6px rgba(0,0,0,0.06);">
            <p style="font-size: 18px; color: #333; margin: 0;">Submission received.</p>
        </div>
    </div>
    <?php
    return ob_get_clean();
}
