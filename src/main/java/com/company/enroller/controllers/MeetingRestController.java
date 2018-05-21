package com.company.enroller.controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

	@Autowired
	ParticipantService participantService;

	// Wersja BASIC
	@RequestMapping(value = "", method = RequestMethod.GET) // pobieranie listy wszystkich spotkań
	public ResponseEntity<?> getMeetings() {

		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET) // pobieranie listy pojedynczego spotkania
	public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST) // dodawanie spotkań
	public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
		if (meetingService.findById(meeting.getId()) != null) {
			return new ResponseEntity("Unable to create. A meeting with id " + meeting.getId() + " already exists.",
					HttpStatus.CONFLICT);
		}
		meetingService.create(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}/participants/{participantId}", method = RequestMethod.PUT) // dodawanie uczestnika do spotkania
	public ResponseEntity<?> addParticipant(@PathVariable("id") Long id, @PathVariable("participantId") String login) {
		Meeting meeting = meetingService.findById(id);
		Participant participant = participantService.findByLogin(login);
		if (meeting == null || participant == null) {
			return new ResponseEntity("Unable to update. A meeting with id " + id + " does not exist.", HttpStatus.NOT_FOUND);
		} 
		meetingService.addParticipant(id, participant);	
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET) // pobieranie uczestników spotkania
	public ResponseEntity<?> getParticipantsFromMeeting(@PathVariable("id") Long id) {
		Meeting meeting = meetingService.findById(id);
		if(meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
	} 
	
	// Wersja GOLD
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE) // usuwanie spotkań
	public ResponseEntity<?> deleteMeetingById(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meetingService.deleteMeeting(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT) // aktualizacja spotkań
	public ResponseEntity<?> updateMeetingById(@PathVariable("id") long id,
			@RequestBody Meeting meetingUpdate) {
		Meeting oldMeeting = meetingService.findById(id);
		if (oldMeeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meetingUpdate.setId(id);
		meetingService.updateMeeting(meetingUpdate);
		return new ResponseEntity<Meeting>(meetingUpdate, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/participants/{participantId}", method = RequestMethod.DELETE) // usuwanie uczestnika ze spotkania
	public ResponseEntity<?> removeParticipant(@PathVariable("id") Long id, @PathVariable("participantId") String login) {
		Meeting meeting = meetingService.findById(id);
		Participant participant = participantService.findByLogin(login);
		if (meeting == null || participant == null) {
			return new ResponseEntity("Unable to delete", HttpStatus.NOT_FOUND);
		} 
		meetingService.removeParticipant(id, participant);	
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
}
}