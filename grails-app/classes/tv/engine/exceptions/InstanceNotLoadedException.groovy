package tv.engine.exceptions

class InstanceNotLoadedException extends BaseException {
    protected static final int code = 416
    private static final long serialVersionUID = 1L

    public InstanceNotLoadedException(String msg = "InstanceNotLoadedException Not Found") {
        super(msg);
    }

    public Integer getCode(){
        return this.code
    }
}
