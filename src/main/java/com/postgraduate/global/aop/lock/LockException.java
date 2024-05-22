package com.postgraduate.global.aop.lock;

import com.postgraduate.global.exception.ApplicationException;

public class LockException extends ApplicationException {
    protected LockException() {
        super("Lock 획득중 예외 발생" , "EX_LOCK");
    }
}
