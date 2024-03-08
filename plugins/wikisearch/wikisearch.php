<?php
/*
Plugin Name: WikiSearch Links
Plugin URI: https://yourwebsite.com/wikisearch-links
Description: Transforms specified hyperlinks into interactive elements for Wikipedia searches.
Version: 1.0
Author: Your Name
Author URI: https://yourwebsite.com
*/

// Enqueue the JavaScript and CSS files
function wikisearch_enqueue_scripts() {
    wp_enqueue_script('wikisearch-js', plugin_dir_url(__FILE__) . 'wikisearch.js', array(), '1.0', true);
    wp_enqueue_style('wikisearch-css', plugin_dir_url(__FILE__) . 'wikisearch.css');
}

add_action('wp_enqueue_scripts', 'wikisearch_enqueue_scripts');
?>
