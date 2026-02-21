# Secure File Transfer System 

A secure and user-friendly file transfer application built using Java, AES & RSA encryption, OpenSSL, and a clean Graphical User Interface (JavaFX/Swing).
This project enables two systems on a local network to exchange files safely, ensuring confidentiality, integrity, and authentication.

# 🚀 Features

End-to-End Encryption

AES-256 for fast symmetric encryption of files

RSA-2048 for secure key exchange

OpenSSL for generating and managing cryptographic keys

Two-Way File Transfer

Send and receive files across a local network

Supports all file types

Interactive GUI

Simple, clean interface for selecting files, connecting to server, and viewing status

Progress indicators for file transfer

Secure Networking

Socket-based communication wrapped with encrypted streams

Prevents man-in-the-middle attacks through key validation

Cross-Platform

Works on Windows, macOS, and Linux with Java 8+

# 🛠️ Prerequisites

Before running the project, ensure you have:

System Requirements

Java 8 or later

OpenSSL installed

A local network (Wi-Fi or LAN) for two devices to communicate

Cryptography Requirements

Run the following commands to generate RSA key pairs using OpenSSL:

openssl genrsa -out private_key.pem 2048
openssl rsa -in private_key.pem -pubout -out public_key.pem

Java Libraries Used

javax.crypto (AES, RSA)

java.net (Networking)

JavaFX / Swing (GUI)

# 📁 Project Structure
Secure-File-Transfer/
│
├── src/
│   ├── encryption/
│   │   ├── AESUtil.java
│   │   ├── RSAUtil.java
│   │   └── KeyManager.java

│   ├── network/
│   │   ├── Server.java
│   │   ├── Client.java
│   │   └── FileTransferHandler.java

│   ├── ui/
│   │   ├── MainUI.java
│   │   └── Controller.java

│   ├── utils/
│   │   └── Logger.java

├── keys/
│   ├── private_key.pem
│   └── public_key.pem
│
├── README.md
└── LICENSE

▶️ Usage
1️⃣ Start the Server

Run the server program:

java Server


Server displays:

IP Address

Port

Waiting for incoming connection

2️⃣ Start the Client

Open the client GUI:

java MainUI


Enter:

Server IP

Port

Click Connect.

3️⃣ Select and Send File

Choose any file via GUI

Client encrypts using AES

AES key is encrypted using RSA

File is transferred securely

4️⃣ Receive File

Server accepts request

Decrypts AES key

Decrypts file

Saves output safely

# 🔐 Security Workflow

AES key is generated per session

File is encrypted using AES-256

AES key is encrypted using RSA public key

Encrypted file + encrypted key is transmitted

Server decrypts AES key using private RSA key

Server decrypts file

This ensures:

Confidentiality

No key leakage

Secure transfer

Replay/Interception protection

# 🧪 Testing

To test the application:

Run server on Device A

Run client on Device B

Connect using LAN IP

Try sending:

Small text files

Images

PDFs

ZIPs

Verify integrity of received output

Test with invalid keys or wrong IP to confirm error handling

# 🛤️ Future Enhancements

Support for TLS-based transfer

Multi-client support

Cloud/Internet transfer mode

Hash verification using SHA-256

Logging dashboard
