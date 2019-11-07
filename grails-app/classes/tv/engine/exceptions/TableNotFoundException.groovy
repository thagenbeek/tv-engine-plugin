package tv.engine.exceptions

class TableNotFoundException extends BaseException {
    protected static final int code = 417
    private static final long serialVersionUID = 1L

    public TableNotFoundException(String msg = "Table Not Found") {
        super(msg);
    }

    public Integer getCode(){
        return this.code
    }
}
