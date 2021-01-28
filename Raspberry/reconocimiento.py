import boto3


def upload_file(file_name, bucket):  
client = boto3.client('rekognition')
response=client.detect_faces(
    Image={
       
        'S3Object': {
                'Bucket' : bucket,
                'Name' : file_name
                            
            }
        },
    Attributes=[
          'ALL'
        ]
    )
print(response)
print("listo")