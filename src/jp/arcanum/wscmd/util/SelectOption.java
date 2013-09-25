package jp.arcanum.wscmd.util;

import java.io.Serializable;

public class SelectOption implements Serializable{

    private String _code;
    private String _value;

    public SelectOption(final String code, final String value){
        _code = code;
        _value = value;
    }

    public final String getCode(){
        return _code;
    }

    public final String getValue(){
        return _value;
    }

}
