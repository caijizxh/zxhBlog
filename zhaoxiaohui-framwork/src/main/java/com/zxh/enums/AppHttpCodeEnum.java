package com.zxh.enums;

public enum AppHttpCodeEnum {
    //成功
    SUCCESS(200,"操作成功"),
    //登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),EMAIL_EXIST(503,"邮箱已存在"),
    REQUIRE_USERNAME(504,"必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    COMMENT_CONTENT_NOT_NULL(506,"评论内容不能为空"),
    USERNAME_NULL(508,"必需填写用户名"),
    PASSWORD_NULL(509,"密码为空，请检查后再进行注册"),
    NICKNAME_NULL(510,"用户昵称为空，请检查后再进行注册"),
    EMAIL_NULL(511,"邮箱为空，请检查后再进行注册"),
    NICKNAME_EXIST(512,"昵称已存在已存在"),
    UPLOAD_FILETYPE_ERROR(507,"上传文件不是png类型，请确认后进行上传");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }
}
