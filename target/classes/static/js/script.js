/* ============================================================
   Restaurant Management System — interface behaviour
   Progressive enhancement only: every page still works with
   JavaScript switched off.
   ============================================================ */

(function () {
    'use strict';

    var reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    document.addEventListener('DOMContentLoaded', function () {
        retireAlerts();
        confirmDestructiveLinks();
        guardAgainstDoubleSubmit();
        validateLoginInline();
        addTableFilters();
    });

    /* ------------------------------------------------------------
       Alerts dismiss themselves, and can be dismissed by hand.
       Anything the user is hovering stays put.
       ------------------------------------------------------------ */
    function retireAlerts() {
        document.querySelectorAll('.alert').forEach(function (alert) {
            if (alert.classList.contains('alert-keep')) return;

            var hovered = false;
            alert.addEventListener('mouseenter', function () { hovered = true; });
            alert.addEventListener('mouseleave', function () { hovered = false; });
            alert.addEventListener('click', function () { dismiss(alert); });
            alert.style.cursor = 'pointer';
            alert.title = 'Dismiss';

            window.setTimeout(function tick() {
                if (hovered) {
                    window.setTimeout(tick, 1500);
                    return;
                }
                dismiss(alert);
            }, 5000);
        });
    }

    function dismiss(el) {
        if (reduceMotion) {
            el.remove();
            return;
        }
        el.style.transition = 'opacity .32s ease, transform .32s ease, margin .32s ease';
        el.style.opacity = '0';
        el.style.transform = 'translateY(-8px)';
        el.style.marginBottom = '0';
        window.setTimeout(function () { el.remove(); }, 330);
    }

    /* ------------------------------------------------------------
       Deleting a record asks first. The templates use plain
       links for delete, so this is the only thing standing
       between a stray click and a lost row.
       ------------------------------------------------------------ */
    function confirmDestructiveLinks() {
        document.querySelectorAll('a[href*="/delete/"]').forEach(function (link) {
            link.addEventListener('click', function (event) {
                var row = link.closest('tr');
                var label = row ? (row.children[1] ? row.children[1].textContent.trim() : '') : '';
                var message = label
                    ? 'Delete "' + label + '"? This cannot be undone.'
                    : 'Delete this record? This cannot be undone.';
                if (!window.confirm(message)) {
                    event.preventDefault();
                }
            });
        });
    }

    /* ------------------------------------------------------------
       A slow save used to let people click Save twice and create
       two rows. The button locks after the first click.
       ------------------------------------------------------------ */
    function guardAgainstDoubleSubmit() {
        document.querySelectorAll('form').forEach(function (form) {
            form.addEventListener('submit', function () {
                var button = form.querySelector('button[type="submit"], input[type="submit"]');
                if (!button || button.dataset.busy) return;

                // Let the browser's own required-field check win first.
                if (typeof form.checkValidity === 'function' && !form.checkValidity()) return;

                button.dataset.busy = '1';
                button.dataset.idleLabel = button.textContent;
                button.disabled = true;
                if (button.textContent.trim()) {
                    button.textContent = 'Working\u2026';
                }

                // If the browser blocks navigation for any reason, give
                // the button back rather than leaving it dead.
                window.setTimeout(function () {
                    if (!button.isConnected) return;
                    button.disabled = false;
                    button.textContent = button.dataset.idleLabel;
                    delete button.dataset.busy;
                }, 8000);
            });
        });
    }

    /* ------------------------------------------------------------
       Login: show the problem next to the field instead of
       firing a browser alert box.
       ------------------------------------------------------------ */
    function validateLoginInline() {
        var form = document.querySelector('form[action*="login"]');
        if (!form) return;

        var username = form.querySelector('#username');
        var password = form.querySelector('#password');
        if (!username || !password) return;

        form.addEventListener('submit', function (event) {
            var missing = null;
            if (!username.value.trim()) missing = username;
            else if (!password.value.trim()) missing = password;
            if (!missing) return;

            event.preventDefault();
            missing.classList.add('is-invalid');
            missing.focus();

            if (!reduceMotion) {
                missing.animate(
                    [
                        { transform: 'translateX(0)' },
                        { transform: 'translateX(-5px)' },
                        { transform: 'translateX(5px)' },
                        { transform: 'translateX(0)' }
                    ],
                    { duration: 260, easing: 'ease-in-out' }
                );
            }

            missing.addEventListener('input', function once() {
                missing.classList.remove('is-invalid');
                missing.removeEventListener('input', once);
            });
        });
    }

    /* ------------------------------------------------------------
       Any table with more than eight rows gets a filter box.
       Typing hides rows that don't match — no page reload, and
       no template changes needed.
       ------------------------------------------------------------ */
    function addTableFilters() {
        document.querySelectorAll('table.table').forEach(function (table) {
            var body = table.querySelector('tbody');
            if (!body) return;

            var rows = Array.prototype.slice.call(body.rows);
            if (rows.length < 8) return;

            var wrap = document.createElement('div');
            wrap.className = 'mb-2 d-flex justify-content-end';

            var input = document.createElement('input');
            input.type = 'search';
            input.className = 'form-control form-control-sm';
            input.style.maxWidth = '260px';
            input.placeholder = 'Filter these rows';
            input.setAttribute('aria-label', 'Filter table rows');

            var count = document.createElement('span');
            count.className = 'text-muted small align-self-center me-2';

            wrap.appendChild(count);
            wrap.appendChild(input);

            var anchor = table.closest('.table-responsive') || table;
            anchor.parentNode.insertBefore(wrap, anchor);

            input.addEventListener('input', function () {
                var term = input.value.trim().toLowerCase();
                var shown = 0;
                rows.forEach(function (row) {
                    var match = !term || row.textContent.toLowerCase().indexOf(term) !== -1;
                    row.hidden = !match;
                    if (match) shown++;
                });
                count.textContent = term ? shown + ' of ' + rows.length : '';
            });
        });
    }
})();
