#!/bin/sh
# Gera PNG e SVG de todos os diagramas em img/.
# Requer o Mermaid CLI: npm i -g @mermaid-js/mermaid-cli   (ou: brew install mermaid-cli)
# Se o puppeteer não achar o Chrome, rode: npx puppeteer browsers install chrome-headless-shell
set -e
cd "$(dirname "$0")"
mkdir -p img
for f in *.mmd; do
	n="${f%.mmd}"
	echo "-> $n"
	mmdc -q -i "$f" -o "img/$n.png" -b white -s 2
	mmdc -q -i "$f" -o "img/$n.svg" -b white
done
