package com.mapotempo.fleet.core.accessor;

import com.couchbase.lite.*;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.exception.CoreException;

import java.util.*;

/**
 * Access.
 */
public class Access<T> {

    private DatabaseHandler mDatabaseHandler;

    private Class<T> mClazz;

    private View mView;

    private DocumentBase mDocumentAnnotation;

    private Factory<T> mFactory;

    private Analyzer<T> mAnalyzer;

    public Access(Class<T> clazz, DatabaseHandler dbHandler) throws CoreException {

        mDatabaseHandler = dbHandler;

        mClazz = clazz;

        mFactory = new Factory<>(mClazz, mDatabaseHandler);

        mAnalyzer = new Analyzer<>(mClazz);

        mDocumentAnnotation = mClazz.getAnnotation(DocumentBase.class);

        if(mDocumentAnnotation == null)
            throw new CoreException("e");

        mView = mDatabaseHandler.mDatabase.getView(mClazz.getSimpleName());

        mView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                Object type_found = document.get(mDocumentAnnotation.type_field());

                if(type_found != null && type_found.toString().equals(mDocumentAnnotation.type()))
                    emitter.emit(document, document.get("_id"));
            }
        }, "2");
    }

    /**
     * commit.
     * @param data the data to commit
     * @return true if data_sample successfully add
     */
    public boolean commit(T data) throws CoreException
    {
        Map<String, Object> mapData = mAnalyzer.getData(data);

        // TODO UPDATE
        String docId  = (String)mapData.get("_id");
        if(docId == null || docId.isEmpty()) {
            docId = mClazz.getSimpleName() + "_" + UUID.randomUUID().toString();
            mapData.put("_id", docId);
        }

        Document document = mDatabaseHandler.mDatabase.getDocument(docId);
        UnsavedRevision update = document.createRevision();
        try {
            update.setProperties(mapData);
            update.save();
            return true;
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * delete.
     * @param id the document id
     * @return true if successfully delete
     */
    public boolean delete(String id) throws CoreException
    {
        Document doc = mDatabaseHandler.mDatabase.getExistingDocument(id);
        if(doc != null) {
            try {
                return doc.delete();
            } catch (CouchbaseLiteException e) {
                throw new CoreException(e);
            }
        }
        return false;
    }

    /**
     * deleteAll.
     * @return true if successfully delete
     */
    public boolean deleteAll() throws CoreException
    {
        try {
            Query query = mView.createQuery();
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                row.getDocument().delete();
            }
            return true;
        } catch (CouchbaseLiteException e) {
            return false;
        }
    }

    /**
     * get.
     * @param id the document id
     * @return the specific T data
     */
    public T get(String id) throws CoreException
    {
        Document doc = mDatabaseHandler.mDatabase.getExistingDocument(id);
        if(doc != null)
            return mFactory.getInstance(doc);
        return null;
    }

    /**
     * getAll.
     * Type view filter
     * @return all data T in a list
     */
    public List<T> getAll() throws CoreException
    {
        Query query = mView.createQuery().toLiveQuery();
        return runQuery(query);
    }

    /**
     * runQuery.
     * @param query the query to run
     * @return a list of T
     **/
    protected List<T> runQuery(Query query) throws CoreException
    {
        List<T> res = new ArrayList<>();
        try {
            QueryEnumerator result = query.run();

            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                String docId = row.getDocumentId();
                Document doc = mDatabaseHandler.mDatabase.getDocument(docId );
                T data = mFactory.getInstance(doc);
                if(data != null)
                    res.add(data);
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return res;
    }

    public DatabaseHandler getDatabaseHandler() {
        return mDatabaseHandler;
    }

    public Class<T> getClazz() {
        return mClazz;
    }

    public View getView() {
        return mView;
    }
}
