import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from decouple import config

class MailAgent():
    @staticmethod
    def send_email(subject, data):
        mail_from = config("MAIL_FROM")
        mail_to = config("MAIL_TO")

        # Construct the email
        msg = MIMEMultipart()
        msg['From'] = mail_from
        msg['To'] = mail_to
        msg['Subject'] = subject

        body = data
        msg.attach(MIMEText(body, 'plain'))

        # Connect to an external SMTP server
        smtp_host = config("SMTP_HOST") 
        smtp_port = config("SMTP_PORT")
        smtp_username = config("MAIL_CLIENT_USER")
        smtp_password = config("MAIL_CLIENT_PASS")

        mailServer = smtplib.SMTP(smtp_host, smtp_port) 
        mailServer.starttls()
        mailServer.login(smtp_username, smtp_password)
        mailServer.sendmail(mail_from, mail_to, msg.as_string())
        mailServer.close()

if __name__ == "__main__":
    MailAgent.send_email("Test subject", "This is a test")