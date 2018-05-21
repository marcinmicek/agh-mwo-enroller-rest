package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}
	
	public Meeting findById(Long id) {
		return (Meeting) connector.getSession().get(Meeting.class, id);
	}
	
	public void create(Meeting meeting) {		
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}
	
	public void deleteMeeting(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(meeting);
		transaction.commit();
	}
	
	public void updateMeeting(Meeting meetingUpdate) {	
        Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(meetingUpdate);
		transaction.commit();
	}
	
	public void addParticipant(Long id, Participant participant) {
		Meeting meeting = findById(id);
		meeting.addParticipant(participant);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().update(meeting);
		transaction.commit();
	}
	
	public void removeParticipant(Long id, Participant participant) {
		Meeting meeting = findById(id);
		meeting.removeParticipant(participant);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().update(meeting);
		transaction.commit();
}

}
