import boto3


client = boto3.client('rekognition')
response=client.index_faces(
    CollectionId='Sam',
    Image={
       
        'S3Object': {
                'Bucket' : 'raspsam',
                'Name' : 'Sam17.jpg'
                            
            }
        },
    ExternalImageId='Brenda',
    DetectionAttributes=[
          'ALL'
        ],
    MaxFaces=1,
    QualityFilter='AUTO'
    )
print(response)
print("listo")