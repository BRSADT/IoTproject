import boto3


client = boto3.client('rekognition')
response=client.search_faces_by_image(
    CollectionId='ColeccionGlobal',
    Image={
       
        'S3Object': {
                'Bucket' : 'raspsam',
                'Name' : 'Sam12.jpg'
                            
            }
        },
   MaxFaces=5,
    QualityFilter='AUTO'
    )
print(response)
print("listo")