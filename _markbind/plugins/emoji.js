module.exports = {
    extendMarkdown: (md) => {
        // Enable markdown-it-emoji
        md.use(require('markdown-it-emoji'));
    },
};
