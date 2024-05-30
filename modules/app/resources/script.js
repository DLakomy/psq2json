const { Converter } = require(
  process.env.NODE_ENV == 'production' ?
    '../target/scala-3.3.3/app-opt.js' :
    '../target/scala-3.3.3/app-fastopt.js'
);

const input = document.getElementById('inputPlsql');
const output = document.getElementById('resultPlsql');

document.getElementById('generateBtn').onclick = () => {
  const { error, result } = Converter.convert(input.value);
  output.setAttribute('aria-invalid', !result);
  output.value = result || error;
};

input.oninput = () => {
  output.value = '';
  output.removeAttribute('aria-invalid');
};
