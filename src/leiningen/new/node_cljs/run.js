try {
    require("source-map-support").install();
} catch(err) {
}
require("./js/goog/bootstrap/nodejs")
require("./{{name}}")
require("./js/{{sanitized}}/core")
{{sanitized}}.core._main(); ;; TODO: fix this assumption
