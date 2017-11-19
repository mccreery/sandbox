/* Usage: jsonmin [options] [input] [output]
 * Example: jsonmin -cjs file.json
 *
 * JSON minifier but faster than JSMin (tm)
 * Doesn't minify javascript, only json
 *
 * input can be a filename or _ for stdin
 * default is _
 *
 * output can be a filename or _ for stdout
 * default is input.min.(extension) or _ if input is stdin
 *
 * options:
 * -r --raw                 output directly instead of using a buffer
 * -k --compress_keywords   convert null, false, true to 0, 0, 1
 * -u --unsafe              don't check for invalid values (no errors)
 * -q --unquoted            allow unquoted names
 * -d --preserve_duplicates don't remove duplicate fields
 * -e --preserve_escapes    don't replace unnecessary escapes

 * -c --comments            enable comment removal
 *               js         allow javascript-style /**\/ and // comments
 *               underscore allow fields starting with _ as comments */

int main(int argc, char** argv) {
	
}
