import { Converter } from '../target/scala-3.3.3/app-fastopt/main.js';

const input = document.getElementById('inputPsql');
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
