package ttl.larku.dao.inmemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Track;

public class InMemoryTrackDAO implements BaseDAO<Track> {

	private Map<Integer, Track> tracks = new HashMap<Integer, Track>();
	private static int nextId = 0;
	
	@Override
	public void update(Track updateObject) {
		if(tracks.containsKey(updateObject.getId())) {
			tracks.put(updateObject.getId(), updateObject);
		}
	}

	@Override
	public void delete(Track track) {
		tracks.remove(track.getId());
	}

	@Override
	public Track create(Track newObject) {
		//Create a new Id
		int newId = ++nextId;
		newObject.setId(newId);
		tracks.put(newId, newObject);
		
		return newObject;
	}

	@Override
	public Track get(int id) {
		return tracks.get(id);
	}

	@Override
	public List<Track> getAll() {
		return new ArrayList<Track>(tracks.values());
	}
	
	@Override
	public void deleteStore() {
		tracks = null;
	}
	
	@Override
	public void createStore() {
		tracks = new HashMap<Integer, Track>();
		nextId = 0;
	}

	public Map<Integer, Track> getTracks() {
		nextId = 0;
		return tracks;
	}

	public void setTracks(Map<Integer, Track> tracks) {
		this.tracks = tracks;
	}
}
