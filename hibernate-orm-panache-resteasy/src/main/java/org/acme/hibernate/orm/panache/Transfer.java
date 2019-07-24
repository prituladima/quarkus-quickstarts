package org.acme.hibernate.orm.panache;

public class Transfer {
    public Long fromHolderId;
    public Long toHolderId;
    public Integer sum;

    public Transfer() {
    }

    public Transfer(Long fromHolderId, Long toHolderId, Integer sum) {
        this.fromHolderId = fromHolderId;
        this.toHolderId = toHolderId;
        this.sum = sum;
    }
}
