package com.example.notes.data

class MyCustomDebuggingException(
    msg: String
): Exception(
    "\n\nMy Custom Debugging Message: $msg\n\n"
)