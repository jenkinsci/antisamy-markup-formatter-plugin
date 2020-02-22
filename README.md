# OWASP Markup Formatter Plugin
## a.k.a. "Safe HTML" Formatter Plugin
## a.k.a. antisamy-markup-formatter plugin

This plugin sanitizes HTML sources according to the [OWASP Java HTML Sanitizer](https://github.com/owasp/java-html-sanitizer) and a basic policy allowing limited HTML markup in user-submitted text.

This plugin is bundled in the Jenkins WAR file and will generally be installed when using the setup wizard, or as a dependency of other plugins.

The "Safe HTML" option in the "Global Security Configuration" page of Jenkins CI is provided by this plugin. This is what controls the HTML filtering for your job descriptions, etc.

## Configuration

When installed, 'Safe HTML' can be selected as Markup Formatter in "Manage Jenkins" → "Configure Global Security" → "Markup Formatter":

User-submitted text, like build, job, and view descriptions, will be sanitized by removing potentially dangerous elements.

# See also:

* ["Managing Security" in Jenkins documentation](https://jenkins.io/doc/book/managing/security/)
