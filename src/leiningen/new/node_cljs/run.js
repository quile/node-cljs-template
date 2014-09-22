try {
    require("source-map-support").install();
} catch(err) {
}
require("./js/goog/bootstrap/nodejs")
require("./{{name}}")
require("./js/{{sanitized}}/core")
cljs.core._STAR_main_cli_fn_STAR_(); // Do I really need this?
