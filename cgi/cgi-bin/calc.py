#!/usr/bin/env python3
import os
from datetime import datetime
from http.cookies import SimpleCookie
from urllib.parse import parse_qs, quote, unquote
from html import escape

MAX_AGE = 30 * 24 * 60 * 60

def read_form_data():
    method = os.environ.get("REQUEST_METHOD", "GET").upper()
    if method == "POST":
        try:
            length = int(os.environ.get("CONTENT_LENGTH", "0"))
        except ValueError:
            length = 0
        raw = os.read(0, length).decode("utf-8", errors="replace")
    else:
        raw = os.environ.get("QUERY_STRING", "")
    parsed = parse_qs(raw, keep_blank_values=True)
    return {k: (v[0] if v else "") for k, v in parsed.items()}

def parse_number(value, label):
    try:
        return float(value)
    except (TypeError, ValueError):
        raise ValueError(f"{label} doit être un nombre valide.")

def compute(a, b, operation):
    if operation == "add":
        return a + b
    if operation == "sub":
        return a - b
    if operation == "mul":
        return a * b
    if operation == "div":
        if b == 0:
            raise ValueError("Division par zéro impossible.")
        return a / b
    raise ValueError("Opération non valide.")

def op_symbol(op):
    return {"add": "+", "sub": "-", "mul": "×", "div": "÷"}.get(op, "?")

def main():
    current_access = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    cookies = SimpleCookie(os.environ.get("HTTP_COOKIE", ""))
    previous_access = cookies.get("last_access")
    previous_value = unquote(previous_access.value) if previous_access else "Aucun accès précédent"

    form = read_form_data()
    a_raw = form.get("a", "")
    b_raw = form.get("b", "")
    op = form.get("op", "")  # FIX: match index.html

    result_text = ""
    error_text = ""

    try:
        a = parse_number(a_raw, "Nombre A")
        b = parse_number(b_raw, "Nombre B")
        result = compute(a, b, op)
        result_text = f"Résultat: {a} {op_symbol(op)} {b} = {result}"
    except ValueError as exc:
        error_text = str(exc)

    print("Content-Type: text/html; charset=utf-8")
    print(f"Set-Cookie: last_access={quote(current_access)}; Max-Age={MAX_AGE}; Path=/")
    print()

    status_class = "error" if error_text else "ok"
    status_content = escape(error_text) if error_text else escape(result_text)

    print("<!doctype html>")
    print("<html lang='fr'>")
    print("<head>")
    print("  <meta charset='UTF-8'>")
    print("  <meta name='viewport' content='width=device-width, initial-scale=1.0'>")
    print("  <title>Résultat - Calculatrice CGI</title>")
    print("  <link rel='stylesheet' href='/styles.css'>")  # FIX: no /www
    print("</head>")
    print("<body>")
    print("  <main class='card'>")
    print("    <h1>Résultat (CGI)</h1>")
    print(f"    <div class='result {status_class}'>{status_content}</div>")
    print(f"    <p class='meta'><strong>Dernier accès:</strong> {escape(previous_value)}</p>")
    print(f"    <p class='meta'><strong>Accès actuel:</strong> {escape(current_access)}</p>")
    print("    <p><a class='back' href='/index.html'>Nouvelle opération</a></p>")  # FIX
    print("  </main>")
    print("</body>")
    print("</html>")

if __name__ == "__main__":
    main()