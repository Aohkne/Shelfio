package com.huukhoa.backend.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    // SERVER
    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_MESSAGE_KEY(400, "Invalid message key", HttpStatus.BAD_REQUEST),

    // AUTH
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN_TYPE(401, "Invalid token type", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),

    // USER
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),

    USER_USERNAME_ALREADY_EXISTS(400, "Username already exists", HttpStatus.BAD_REQUEST),
    USER_USERNAME_LIMIT_EXCEEDED(400, "Username limit exceeded", HttpStatus.BAD_REQUEST),
    USER_EMAIL_ALREADY_EXISTS(400, "Email already exists", HttpStatus.BAD_REQUEST),
    USER_EMAIL_NOT_VALID(400, "Email not valid", HttpStatus.BAD_REQUEST),
    USER_PASSWORD_LIMIT_EXCEEDED(400, "Password must be a from 8 to 50 char", HttpStatus.BAD_REQUEST),
    INVALID_DOB(400, "Your age must be a least {min} years old", HttpStatus.BAD_REQUEST),

    // ROLE
    ROLE_NOT_FOUND(404, "role not found", HttpStatus.NOT_FOUND),
    ROLE_ALREADY_EXISTS(400, "Role already exists", HttpStatus.BAD_REQUEST),
    ROLE_NAME_INVALID(400, "Role name invalid", HttpStatus.BAD_REQUEST),

    // PERMISSION
    PERMISSION_NOT_FOUND(404, "Permission not found", HttpStatus.NOT_FOUND),
    PERMISSION_ALREADY_EXISTS(400, "Permission already exists", HttpStatus.BAD_REQUEST),
    PERMISSION_NAME_INVALID(400, "Permission name invalid", HttpStatus.BAD_REQUEST),

    // BOOK
    BOOK_NOT_FOUND(404, "Book not found", HttpStatus.NOT_FOUND),
    BOOK_ISBN_ALREADY_EXISTS(400, "Book with this ISBN already exists", HttpStatus.BAD_REQUEST),
    BOOK_NOT_AVAILABLE(400, "Book is not available for borrowing", HttpStatus.BAD_REQUEST),
    BOOK_CURRENTLY_BORROWED(400, "Book is currently borrowed and cannot be deleted", HttpStatus.BAD_REQUEST),

    // MEMBER
    MEMBER_NOT_FOUND(404, "Member not found", HttpStatus.NOT_FOUND),
    MEMBER_SUSPENDED(400, "Member account is suspended", HttpStatus.BAD_REQUEST),
    MEMBER_ALREADY_BORROWING(400, "Member is already borrowing this book", HttpStatus.BAD_REQUEST),

    // BORROWING
    BORROWING_NOT_FOUND(404, "Borrowing record not found", HttpStatus.NOT_FOUND),
    BORROWING_ALREADY_RETURNED(400, "This book has already been returned", HttpStatus.BAD_REQUEST),
    ;

    int code;
    String message;
    HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
