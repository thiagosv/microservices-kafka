package com.br.alcateiadev.leitorftp.leitorftp.service.ftp;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class TransferFTPService {

    @Value("${ftp.server}")
    private String server;

    @Value("${ftp.user}")
    private String user;

    @Value("${ftp.pass}")
    private String pass;

    @Value("${ftp.port}")
    private String port;

    @Autowired
    private UploadS3Service uploadS3Service;

    public List<FileTransfer> execute() {
        List<FileTransfer> returnList = new ArrayList<>();
        FTPClient ftpClient = new FTPClient();

        try {
            //configuração da conexão com o ftp client
            configuraConexao(ftpClient);

            //lista os arquivos
            String[] arquivos = ftpClient.listNames();
            for(String itemFile: arquivos){
                File tmpDownload = new File(itemFile);

                //baixa o arquivo
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tmpDownload));
                boolean success = ftpClient.retrieveFile(itemFile, outputStream);
                outputStream.close();
                if(success){
                    log.info("Arquivo recebido com sucesso, enviando para o S3");

                    FileTransfer fileTransfer = uploadS3Service.execute(itemFile, tmpDownload);
                    fileTransfer.setPathLocal(tmpDownload);
                    returnList.add(fileTransfer);
                    log.info("Enviado para o s3");

                    ftpClient.deleteFile(itemFile);
                    log.info("Arquivo deletado do servidor.");
                }
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try{
                // disconecta do ftp
                disconectaFtp(ftpClient);
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }

        return returnList;
    }

    private void disconectaFtp(FTPClient ftpClient) throws IOException {
        if(ftpClient.isConnected()){
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    private void configuraConexao(FTPClient ftpClient) throws IOException {
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftpClient.connect(server, Integer.parseInt(port));
        ftpClient.login(user, pass);
        ftpClient.enterLocalActiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    }
}
