package org.beatific.microservice.container.repository;

import org.beatific.microservice.container.utils.JPickle;

public class FileObjectRepository<T> extends ObjectRepository<T> {

	private final String filename;
	public FileObjectRepository(String filename) {
		this.filename = filename;
	}
	
	@Override
	public void saveObject(T object) throws RepositoryException {
		try {
		    JPickle.dump(filename, object);
		} catch(Exception e) {
			throw new RepositoryException("File Save Error [" + filename + "]", e);
		}
	}

	@Override
	public void removeObject(T object, Object key) throws RepositoryException {
		try {
		    JPickle.dump(filename, object);
		} catch(Exception e) {
			throw new RepositoryException("File Remove Error [" + filename + "], key [" + key + "]", e);
		}
	}

	@Override
	public void clearObject() throws RepositoryException {
		try {
		    JPickle.clear(filename);
		} catch(Exception e) {
			throw new RepositoryException("File Clear Error [" + filename + "]", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T loadObject() throws RepositoryException {
		try {
			return (T)JPickle.load(filename);
		} catch (Exception e) {
			throw new RepositoryException("File Load Error [" + filename + "]", e);
		} 
	}

}
