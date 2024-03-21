package com.e2x.bigcommerce.routinefinder.data.repository.bucket

import com.e2x.bigcommerce.routinefinder.data.repository.RoutineGraphReaderWriter
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph
import com.google.cloud.ReadChannel
import com.google.cloud.storage.Blob
import com.google.cloud.storage.Bucket
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import spock.lang.Specification

import static com.e2x.bigcommerce.routinefinder.data.repository.bucket.GcpBucketRoutineGraphRepository.BLOB_NAME

class GcpBucketRoutineGraphRepositorySpec extends Specification {
    GcpBucketRoutineGraphRepository testObj

    RoutineGraphReaderWriter routineGraphReaderWriter
    String bucketName = 'bucket-name'
    StorageOptions storageOptions

    void setup() {
        storageOptions = Mock()
        routineGraphReaderWriter = Mock()

        testObj = new GcpBucketRoutineGraphRepository(bucketName, storageOptions, routineGraphReaderWriter)
        testObj.reset()
    }

    void 'it should load configuration if not loaded'() {
        given:
        def storage = Mock(Storage)
        storageOptions.getService() >> storage

        and:
        def bucket = Mock(Bucket)
        storage.get(bucketName) >> bucket
        def blob = Mock(Blob)
        bucket.get(BLOB_NAME) >> blob
        def readChannel = Mock(ReadChannel)
        blob.reader() >> readChannel

        and:
        def expectedGraph = Mock(RoutineGraph)
        routineGraphReaderWriter.read(_) >> expectedGraph

        when:
        def graph = testObj.fetchCurrent()

        then:
        graph == expectedGraph
    }

    void 'it should not load configuration if already loaded'() {
        given:
        def storage = Mock(Storage)
        storageOptions.getService() >> storage

        and:
        def bucket = Mock(Bucket)
        storage.get(bucketName) >> bucket
        def blob = Mock(Blob)
        bucket.get(BLOB_NAME) >> blob
        def readChannel = Mock(ReadChannel)
        blob.reader() >> readChannel

        and:
        def graph = Mock(RoutineGraph)
        routineGraphReaderWriter.read(_) >> graph

        when:
        testObj.fetchCurrent()

        then:
        1 * routineGraphReaderWriter.read(_) >> graph

        when:
        testObj.fetchCurrent()

        then:
        0 * routineGraphReaderWriter.read(_) >> graph
    }

    void 'it should throw an exception if the bucket cannot be found'() {
        given:
        def storage = Mock(Storage)
        storageOptions.getService() >> storage

        and:
        def bucket = Mock(Bucket)
        bucket.exists() >> false

        and:
        storage.get(bucketName) >> bucket

        when:
        testObj.save(Mock(RoutineGraph))

        then:
        thrown(RuntimeException)
    }

    void 'it should save the routine graph'() {
        given:
        def storage = Mock(Storage)
        storageOptions.getService() >> storage

        and:
        def bucket = Mock(Bucket)
        bucket.exists() >> true

        and:
        storage.get(bucketName) >> bucket

        when:
        testObj.save(Mock(RoutineGraph))

        then:
        noExceptionThrown()

        and:
        1 * bucket.create(BLOB_NAME, _)
    }
}
