# Search empty directories
find . -type d -empty -not -path "*/.git/*" -not -path "*/target/*" -not -path "*/target_grunt/*"

