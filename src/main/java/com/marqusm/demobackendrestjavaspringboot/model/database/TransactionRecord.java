package com.marqusm.demobackendrestjavaspringboot.model.database;

import com.marqusm.demobackendrestjavaspringboot.annotation.Id;
import com.marqusm.demobackendrestjavaspringboot.annotation.Table;
import com.marqusm.demobackendrestjavaspringboot.enumeration.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/** @author : Marko Mišković */
@AllArgsConstructor(staticName = "of")
@Getter
@Table("transaction")
public class TransactionRecord {
  @Id UUID id;
  UUID sellerAccountId;
  UUID buyerAccountId;
  String externalTransactionId;
  BigDecimal amount;
  String currency;
  String externalTransactionStatus;
  String buyerFullName;
  String buyerAddress;
  String buyerEmail;
  OffsetDateTime createdAt;
  TransactionStatus status;
}
