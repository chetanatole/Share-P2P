package jtorrent.Communication.Requests;

import java.io.Serializable;

/**
 * {@summary} interface for sending request to peers for a particular file
 * 
 * @Implemented by LeechRequest, SeedRequest, ConnectRequest, AnnounceRequest,
 *              ExitRequest, UpdateRequest
 * 
 */

// TODO: rewrite with inheritance instead of interfaces
public interface Request extends Serializable {

    public String getRequestType();

    public String getHostName();

    public Integer getPort();

    public void setHostName(String hostName);
}