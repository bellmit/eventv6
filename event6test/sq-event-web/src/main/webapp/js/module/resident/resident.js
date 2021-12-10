function formatType(value, rec, idx){
	return window["globalDictionary"] ? window["globalDictionary"].getColumnText("I_TYPE", value) : value;
}
function formatMarriage(value, rec, idx){
	return window["globalDictionary"] ? window["globalDictionary"].getColumnText("I_MARRIAGE", value) : value;
}
function formatEducation(value, rec, idx){
	return window["globalDictionary"] ? window["globalDictionary"].getColumnText("EDUCATION_LEVEL", value) : value;
}
function formatSex(value, rec, idx){
	return window["globalDictionary"] ? window["globalDictionary"].getColumnText("I_GENDER", value) : value;
}
function formatEthnic(value, rec, idx){
	return window["globalDictionary"] ? window["globalDictionary"].getColumnText("P_NATION", value) : value;
}