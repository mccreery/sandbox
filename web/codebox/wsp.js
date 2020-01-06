function markupWhitespace(str) {
    const node = (wsp, hint, cls) => {
        hint = hint.padEnd(wsp.length, hint);
        return `<span class="${cls}" data-wsp="${hint}">${wsp}</span>`;
    };

    const tabs = wsp => {
        let markup = node(wsp.slice(0, 1), "→", "wsp tabs");

        if (wsp.length > 1) {
            markup += node(wsp.slice(1), "→", "wsp tabs");
        }
        return markup;
    };

    return escapeHtml(str)
        .replace(/ +/g, wsp => node(wsp, "·", "wsp"))
        .replace(/\t+/g, tabs);
}

function escapeHtml(str) {
    return str
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
