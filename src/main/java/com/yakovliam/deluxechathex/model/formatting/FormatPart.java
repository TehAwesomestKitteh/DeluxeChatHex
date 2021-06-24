package com.yakovliam.deluxechathex.model.formatting;

public class FormatPart {

    /**
     * The part's text
     */
    private String text;

    /**
     * The extra for the format part
     */
    private com.yakovliam.deluxechathex.model.formatting.Extra extra;

    /**
     * Construct format part
     *
     * @param text  text
     * @param extra extra
     */
    public FormatPart(String text, com.yakovliam.deluxechathex.model.formatting.Extra extra) {
        this.text = text;
        this.extra = extra;
    }

    /**
     * Construct format part
     */
    public FormatPart() {
    }

    /**
     * Returns text
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text
     *
     * @param text text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns extra
     *
     * @return extra
     */
    public com.yakovliam.deluxechathex.model.formatting.Extra getExtra() {
        return extra;
    }

    /**
     * Sets extra
     *
     * @param extra extra
     */
    public void setExtra(com.yakovliam.deluxechathex.model.formatting.Extra extra) {
        this.extra = extra;
    }
}
