/*
 * Project senpai
 *
 * Copyright 2023-2023 Alessandro 'kLeZ' Accardo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

let emptyState = null;
let displayedSnippet = null;

function triggerWindowResize() {
    // PrismJs GitHub: https://github.com/PrismJS/prism/issues
    // TODO: A method has been exposed by PrismJS to avoid this event-based approach
    // IMPORTANT: For line number rendering PrismJs -> Issue #2284 PrismJs GitHub
    const event = document.createEvent('HTMLEvents');
    event.initEvent('resize', true, false);
    document.dispatchEvent(event);
    // IMPORTANT: Line highlight triggered before line number -> Issue #2285 PrismJs GitHub
    document.dispatchEvent(event);
}

function showCodeSnippet(key) {
    emptyState.hide();
    const correspondingCodeSnippet = $('div.snippets').find(`[data-key=${key}]`);
    if (displayedSnippet !== correspondingCodeSnippet) {
        if (displayedSnippet != null) displayedSnippet.hide();
        correspondingCodeSnippet.show();
        triggerWindowResize();
        displayedSnippet = correspondingCodeSnippet;
    }
}

function hideCodeSnippet() {
    emptyState.show();
    if (displayedSnippet != null) displayedSnippet.hide();
    displayedSnippet = null;
}

function noop() {
}

$(document).ready(function () {
    emptyState = $('div.snippets-panel-empty-state-layout');
    $('div.comments-panel-header').hover(hideCodeSnippet, noop);
    $('div.general-comment').each(function () {
        $(this).hover(hideCodeSnippet, noop)
    });
    $('div.file-comment').each(function () {
        $(this).hover(function () {
            showCodeSnippet($(this).data('key'))
        }, noop)
    });
    $('div.copyright').hover(hideCodeSnippet, noop);
});
