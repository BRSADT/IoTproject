import boto3


client = boto3.client('rekognition')
response=client.detect_faces(
    Image={
       
        'S3Object': {
                'Bucket' : 'raspsam',
                'Name' : 'foto2.jpg'
                            
            }
        },
    Attributes=[
          'ALL'
        ]
    )
print(response)
print("listo")