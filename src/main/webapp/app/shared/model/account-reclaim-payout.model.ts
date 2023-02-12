import { IManagedAccount } from '@/shared/model/managed-account.model';
import { IAccountReclaimRequest } from '@/shared/model/account-reclaim-request.model';
import { IVoteManager } from '@/shared/model/vote-manager.model';

import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';
export interface IAccountReclaimPayout {
  id?: number;
  amount?: number;
  timestamp?: Date;
  ccy?: VoteCcy;
  txnRef?: string;
  managedAccount?: IManagedAccount | null;
  accountReclaimRequest?: IAccountReclaimRequest | null;
  voteManager?: IVoteManager | null;
}

export class AccountReclaimPayout implements IAccountReclaimPayout {
  constructor(
    public id?: number,
    public amount?: number,
    public timestamp?: Date,
    public ccy?: VoteCcy,
    public txnRef?: string,
    public managedAccount?: IManagedAccount | null,
    public accountReclaimRequest?: IAccountReclaimRequest | null,
    public voteManager?: IVoteManager | null
  ) {}
}
