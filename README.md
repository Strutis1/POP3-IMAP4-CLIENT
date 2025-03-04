# Email Client - Setup Guide

## Prerequisites
To use this email client, you need:
- A **Gmail account**.
- **2-Step Verification** enabled.
- **POP3 access** enabled in Gmail settings.

## Setting Up Gmail (POP3)
### 1. Enable 2-Step Verification & Generate an App Password
1. Go to [Google Security](https://myaccount.google.com/security) and enable **2-Step Verification**.
2. Open [Google App Passwords](https://myaccount.google.com/apppasswords).
3. Select **Mail** as the app and **Windows Computer** as the device.
4. Click **Generate** and copy the 16-character app password.

### 2. Enable POP3 in Gmail
1. Open **Gmail** → Click **⚙ Settings** → **See all settings**.
2. Go to the **"Forwarding and POP/IMAP"** tab.
3. Under **POP Download**, enable **"POP for all mail"**.
4. Click **Save Changes**.

### 3. Configure the Email Client
Use these settings:
```plaintext
Email Address / Username: Your full Gmail address.
Password: The 16-character app password.
```

## Troubleshooting
### Cannot Connect?
- Ensure you are using the **App Password** (not your normal Gmail password).
- **Check Google Security Alerts** for blocked login attempts.
- Open [Google Unlock Captcha](https://accounts.google.com/DisplayUnlockCaptcha) and allow access.
- Try a different network (firewall or ISP might be blocking POP3).

## Future Expansion
- **IMAP Support**: Synchronize emails across multiple devices.
- **Outlook & Other Providers**: Additional configuration guides.
- **SMTP Integration**: Sending emails directly from the client.

Stay tuned for updates!

