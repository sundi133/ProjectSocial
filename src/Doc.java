//package com.Feilds;

//import CodeSheet;

public class Doc {
private int code;
private String coder;
private String newspaper;
private String title;
private boolean eventclosed;
private EventDate eventdate;
private String lengthOfProtest;
private String country;
private String state;
private String location;
private String eventDescription;
private String protestingParties;
private String selfDefinition;
private String issues;
private String protestTarget;
private String targetResponding;
private String protestingOrganization;
private String numberOfProtestors;
private String connectionToOtherEvents;
private String outcome;
private String comments;
private String stateForceAction;
private String numberOfStateForce;
private String numberOfProtestorArrested;
private Injured numberInjured;
private Killed numberKilled;
private String propertyDamage;
private CodeSheet spainTranslate;

public int getCode() {
	return code;
}
public void setCode(int code) {
	this.code = code;
}
public String getCoder() {
	return coder;
}
public void setCoder(String param) {
	this.coder = translate(param);
}
public String getNewspaper() {
	return newspaper;
}
public void setNewspaper(String newspaper) {
	this.newspaper = newspaper;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = translate(title);
}
public boolean isEventclosed() {
	return eventclosed;
}
public void setEventclosed(boolean eventclosed) {
	this.eventclosed = eventclosed;
}
public EventDate getEventdate() {
	return eventdate;
}
public void setEventdate(EventDate eventdate) {
	this.eventdate = eventdate;
}
public String getLengthOfProtest() {
	return lengthOfProtest;
}
public void setLengthOfProtest(String lengthOfProtest) {
	this.lengthOfProtest = translate(lengthOfProtest);
}
public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = translate(country);
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = translate(state);
}
public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = translate(location);
}
public String getEventDescription() {
	return eventDescription;
}
public void setEventDescription(String eventDescription) {
	this.eventDescription = translate(eventDescription);
}
public String getProtestingParties() {
	return protestingParties;
}
public void setProtestingParties(String protestingParties) {
	this.protestingParties = translate(protestingParties);
}
public String getSelfDefinition() {
	return selfDefinition;
}
public void setSelfDefinition(String selfDefinition) {
	this.selfDefinition = translate(selfDefinition);
}
public String getIssues() {
	return issues;
}
public void setIssues(String issues) {
	this.issues = translate(issues);
}
public String getProtestTarget() {
	return protestTarget;
}
public void setProtestTarget(String protestTarget) {
	this.protestTarget = translate(protestTarget);
}
public String getTargetResponding() {
	return targetResponding;
}
public void setTargetResponding(String targetResponding) {
	this.targetResponding = translate(targetResponding);
}
public String getProtestingOrganization() {
	return protestingOrganization;
}
public void setProtestingOrganization(String protestingOrganization) {
	this.protestingOrganization = translate(protestingOrganization);
}
public String getNumberOfProtestors() {
	return numberOfProtestors;
}
public void setNumberOfProtestors(String numberOfProtestors) {
	this.numberOfProtestors = translate(numberOfProtestors);
}
public String getConnectionToOtherEvents() {
	return connectionToOtherEvents;
}
public void setConnectionToOtherEvents(String connectionToOtherEvents) {
	this.connectionToOtherEvents = translate(connectionToOtherEvents);
}
public String getOutcome() {
	return outcome;
}
public void setOutcome(String outcome) {
	this.outcome = translate(outcome);
}
public String getComments() {
	return comments;
}
public void setComments(String comments) {
	this.comments = translate(comments);
}
public String getStateForceAction() {
	return stateForceAction;
}
public void setStateForceAction(String stateForceAction) {
	this.stateForceAction = translate(stateForceAction);
}
public String getNumberOfStateForce() {
	return numberOfStateForce;
}
public void setNumberOfStateForce(String numberOfStateForce) {
	this.numberOfStateForce = translate(numberOfStateForce);
}
public String getNumberOfProtestorArrested() {
	return numberOfProtestorArrested;
}
public void setNumberOfProtestorArrested(String numberOfProtestorArrested) {
	this.numberOfProtestorArrested = translate(numberOfProtestorArrested);
}
public Injured getNumberInjured() {
	return numberInjured;
}
public void setNumberInjured(Injured numberInjured) {
	this.numberInjured = numberInjured;
}
public Killed getNumberKilled() {
	return numberKilled;
}
public void setNumberKilled(Killed numberKilled) {
	
	this.numberKilled = numberKilled;
}
public String getPropertyDamage() {
	return propertyDamage;
}
public void setPropertyDamage(String propertyDamage) {
	this.propertyDamage = translate(propertyDamage);
}
private String translate(String param) {
	// TODO Auto-generated method stub
	if(spainTranslate!=null)
		return spainTranslate.translate(param);

	return null;

}
public void setTranslater(CodeSheet codesheet) {
	spainTranslate = codesheet;
	// TODO Auto-generated method stub
	
}



}
