<?php
/*
Plugin Name: WikiSearch
Plugin URI: https://fishdan.com/wikisearch
Description: Transforms specified hyperlinks into interactive elements for Wikipedia searches.
Version: 0.1.1
Author: Dan Fishman
Author URI: https://fishdan.com
*/

/* Optional but recommended so WP knows updates come from GitHub */
/* Update URI: https://github.com/fishdan/wikisearch */


require __DIR__ . '/plugin-update-checker/plugin-update-checker.php';
$wks_puc = Puc_v4_Factory::buildUpdateChecker(
    'https://github.com/fishdan/wikisearch',
    __FILE__,
    'wikisearch'
);
$wks_puc->getVcsApi()->enableReleaseAssets();

// Enqueue the JavaScript and CSS files
function wikisearch_enqueue_scripts() {
    wp_enqueue_script('wikisearch-js', plugin_dir_url(__FILE__) . 'wikisearch.js', array(), '1.0', true);
    wp_enqueue_style('wikisearch-css', plugin_dir_url(__FILE__) . 'wikisearch.css');
}

add_action('wp_enqueue_scripts', 'wikisearch_enqueue_scripts');
?>
