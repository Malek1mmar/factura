package com.example.fatoura.core.domain.constant;

public class MessageConstants {
    // Error messages
    public static final String INVOICE_NOT_FOUND = "Invoice not found with id: %s";
    public static final String ORGANIZATION_NOT_FOUND = "Organization not found with id: %s";
    public static final String ACCESS_DENIED_INVOICE = "You do not have access to this invoice";
    public static final String ACCESS_DENIED_ORGANIZATION = "You do not have access to this organization";
    
    // Infrastructure error messages
    public static final String FILE_DELETE_ERROR = "Could not delete file";
    public static final String FILE_READ_ERROR = "Could not read file: %s";
    public static final String STORAGE_INIT_ERROR = "Could not initialize storage";
    public static final String OCR_EXTRACT_ERROR = "Failed to extract text from PDF: %s";
}
