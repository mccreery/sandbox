const inputElement = document.getElementById("code-input");
const outputElement = document.getElementById("code-output");

inputElement.onkeydown = function(e) {
    if (e.key == "Tab") {
        e.preventDefault();
        replaceSelection(this, "\t");
    } else if(e.key == "Enter" && e.ctrlKey) {
        e.preventDefault();
        generate();
    }
};

function replaceSelection(textarea, str) {
    const selectionStart = textarea.selectionStart;

    textarea.value = textarea.value.slice(0, textarea.selectionStart) +
        str + textarea.value.slice(textarea.selectionEnd);

    textarea.selectionEnd = selectionStart + str.length;
}

function generate() {
    outputElement.innerHTML = markupWhitespace(inputElement.value);
    hljs.highlightBlock(outputElement);
}
