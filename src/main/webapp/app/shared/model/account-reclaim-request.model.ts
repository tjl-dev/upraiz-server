import { IAccountReclaimPayout } from '@/shared/model/account-reclaim-payout.model';
import { IManagedAccount } from '@/shared/model/managed-account.model';
import { IVoteManager } from '@/shared/model/vote-manager.model';

import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';
export interface IAccountReclaimRequest {
  id?: number;
  amount?: number;
  timestamp?: Date;
  ccy?: VoteCcy;
  active?: boolean | null;
  accountReclaimPayout?: IAccountReclaimPayout | null;
  managedAccount?: IManagedAccount | null;
  voteManager?: IVoteManager | null;
}

export class AccountReclaimRequest implements IAccountReclaimRequest {
  constructor(
    public id?: number,
    public amount?: number,
    public timestamp?: Date,
    public ccy?: VoteCcy,
    public active?: boolean | null,
    public accountReclaimPayout?: IAccountReclaimPayout | null,
    public managedAccount?: IManagedAccount | null,
    public voteManager?: IVoteManager | null
  ) {
    this.active = this.active ?? false;
  }
}
