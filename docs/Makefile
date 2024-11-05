# Generate a single PDF, ePub or HTML from the Markdown source files
#
# In order to use this makefile, you need some tools:
# - GNU make
# - Pandoc
# - LaTeX (texlive-core, texlive-fontsextra)

# Output directory and filename
BUILD = build
PDF_OUTPUT_FILENAME = NjanguiDocumentation
HTML_OUTPUT_FILENAME = index

# Additional Metadata
METADATA = metadata.yml

# Source for the document
SOURCE = $(shell ls src/*.md)

# Combine all the arguments 
ARGS = --from markdown \
		--toc \
		--toc-depth=2 \
		--variable subparagraph \
		--top-level-division=chapter \
		--highlight-style breezedark \
		--citeproc \
		--csl=bibliography.csl

PDF_ARGS = $(ARGS) \
		--template="./templates/eisvogel.tex" \
		-V papersize:a4 \
		-V book \
		-V classoption=oneside \
		--pdf-engine=xelatex \
		--listings 

HTML_ARGS = $(ARGS) \
		--standalone \
		--embed-resources

# Default task
all: clean pdf html

# Build tasks for individual formats
pdf: $(BUILD)/$(PDF_OUTPUT_FILENAME).pdf
html: $(BUILD)/$(HTML_OUTPUT_FILENAME).html

# Clean the build directory
.PHONY : clean
clean:
	rm -rf $(BUILD)

# Build a pdf
$(BUILD)/$(PDF_OUTPUT_FILENAME).pdf: $(METADATA) $(SOURCE)
	mkdir -p $(BUILD)
	pandoc $(PDF_ARGS) -o $@ $^

# Build an HTML file
$(BUILD)/$(HTML_OUTPUT_FILENAME).html: $(METADATA) $(SOURCE)
	mkdir -p $(BUILD)
	pandoc $(HTML_ARGS) -o $@ $^