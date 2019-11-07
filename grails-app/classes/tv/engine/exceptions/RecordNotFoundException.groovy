package tv.engine.exceptions

class RecordNotFoundException extends BaseException {
    protected static final int code = 415
    private static final long serialVersionUID = 1L

    public RecordNotFoundException(String msg = "Record Not Found") {
        super(msg);
    }

    public Integer getCode(){
        return this.code
    }
}
