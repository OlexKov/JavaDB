package org.example;

import lombok.Data;

@Data
public class Abonent {
    private int id;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------------------------------------------");
        sb.append('\n');
        sb.append("\tID:      ");
        sb.append(id);
        sb.append('\n');
        sb.append("\tIм'я:    ");
        sb.append(name);
        sb.append('\n');
        sb.append("\tEmail:   ");
        sb.append(email);
        sb.append('\n');
        sb.append("\tТелефон: ");
        sb.append(phoneNumber);
        sb.append('\n');
        sb.append("\tАдреса:  ");
        sb.append(address);
        sb.append('\n');
        return sb.toString();
    }
}
