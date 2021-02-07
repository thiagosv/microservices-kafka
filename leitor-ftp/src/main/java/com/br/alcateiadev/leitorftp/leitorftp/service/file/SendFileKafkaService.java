package com.br.alcateiadev.leitorftp.leitorftp.service.file;

import com.br.alcateiadev.leitorftp.leitorftp.gateway.json.FileUUIDJson;
import com.br.alcateiadev.leitorftp.leitorftp.kafka.KafkaRequestSender;
import com.br.alcateiadev.leitorftp.leitorftp.service.ftp.FileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

@Service
public class SendFileKafkaService {

    public static final String SISTEMA_1 = "[SISTEMA1]";
    public static final String SISTEMA_2 = "[SISTEMA2]";

    @Value("${kafka.topictopics1}")
    private String topictopics1;

    @Value("${kafka.topictopics2}")
    private String topictopics2;

    @Autowired
    private KafkaRequestSender kafkaRequestSender;

    public void execute(FileTransfer fileTransfer) throws FileNotFoundException {
        //pega a primeira linha do arquivo
        Scanner in = new Scanner(fileTransfer.getPathLocal());
        String firstLine = null;
        while(in.hasNextLine()){
            firstLine = in.nextLine();
            break;
        }

        String topic = null;
        if(firstLine.equals(SISTEMA_1))
            topic = topictopics1;
        else if(firstLine.equals(SISTEMA_2))
            topic = topictopics2;

        kafkaRequestSender.sendMessage(topic,
                FileUUIDJson
                        .builder()
                        .uuid(fileTransfer.getUuid())
                        .build()
        );

    }
}
