package tv.engine.exceptions

class GetException extends BaseException {
    protected static final int code = 418
    private static final long serialVersionUID = 1L

    public GetException(String msg = "Error During GET") {
        super(msg);
    }

    public Integer getCode(){
        return this.code
    }
}
