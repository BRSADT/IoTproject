import boto3
from botocore.exceptions import ClientError


def upload_file(file_name, bucket, object_name=None):
   
    print("S3")
    # If S3 object_name was not specified, use file_name
    if object_name is None:
        object_name = file_name

    # Upload the file
    s3_client = boto3.client('s3')
    try:
        
        response = s3_client.upload_file(file_name, bucket, object_name)
       
    except ClientError as e:
        logging.error(e)
        print(e)
        return False
    return True

print("hola")
upload_file("/home/pi/Desktop/fotoCara.jpg",'raspsam',"FotosAnalisis/FotoCara.jpg")