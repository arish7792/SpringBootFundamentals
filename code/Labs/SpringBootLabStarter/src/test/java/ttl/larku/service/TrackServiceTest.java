package ttl.larku.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ttl.larku.domain.Track;
import ttl.larku.jconfig.LarkUConfig;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({ "classpath:applicationContext.xml" })
@ContextConfiguration(classes=LarkUConfig.class)
@ActiveProfiles("development")
public class TrackServiceTest {

	@Resource
	private TrackService trackService;
	
	@Before
	public void setup() {
		trackService.clear();
	}
	
	@Test
	public void testCreateTrack() {
		Track newTrack = trackService.createTrack("You Stepped Out Of A Dream", "Herb Ellis", "Three guitars in Bossa Nova Time",
				"04:19", "1963");
		newTrack = trackService.createTrack(newTrack);
		
		Track result = trackService.getTrack(newTrack.getId());
		
		assertTrue(result.getTitle().contains(newTrack.getTitle()));
		assertEquals(1, trackService.getAllTracks().size());
	}
	
	@Test
	public void testDeleteTrack() {
		Track track1 = Track.title("The Shadow Of Your Smile").artist("Big John Patton")
				.album("Let 'em Roll").duration("06:15").date("1965").build();
		track1 = trackService.createTrack(track1);
		Track track2 = Track.title("I'll Remember April").artist("Jim Hall and Ron Carter")
				.album("Alone Together").duration("05:54").date("1972").build();
		track2 = trackService.createTrack(track2);
		
		assertEquals(2, trackService.getAllTracks().size());
		
		trackService.deleteTrack(track1.getId());
		
		assertEquals(1, trackService.getAllTracks().size());
		assertTrue(trackService.getAllTracks().get(0).getTitle().contains("April"));
	}

	@Test
	public void testDeleteNonExistentTrack() {
		Track track1 = Track.title("The Shadow Of Your Smile").artist("Big John Patton")
				.album("Let 'em Roll").duration("06:15").date("1965").build();
		track1 = trackService.createTrack(track1);
		Track track2 = Track.title("I'll Remember April").artist("Jim Hall and Ron Carter")
				.album("Alone Together").duration("05:54").date("1972").build();
		track2 = trackService.createTrack(track2);
		
		assertEquals(2, trackService.getAllTracks().size());
		
		trackService.deleteTrack(9999);
		
		assertEquals(2, trackService.getAllTracks().size());
	}
	
	@Test
	public void testUpdateTrack() {
		Track track1 = Track.title("The Shadow Of Your Smile").artist("Big John Patton")
				.album("Let 'em Roll").duration("06:15").date("1965").build();
		
		track1 = trackService.createTrack(track1);

		assertEquals(1, trackService.getAllTracks().size());
		
		track1.setTitle("A Shadowy Smile");
		trackService.updateTrack(track1);
		
		assertEquals(1, trackService.getAllTracks().size());
		assertTrue( trackService.getAllTracks().get(0).getTitle().contains("Shadowy"));
	}
}
