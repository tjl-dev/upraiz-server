import { IAccountReclaimPayout } from '@/shared/model/account-reclaim-payout.model';
import { IVoteManager } from '@/shared/model/vote-manager.model';
import { IAccountReclaimRequest } from '@/shared/model/account-reclaim-request.model';

import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';
export interface IManagedAccount {
  id?: number;
  accountName?: string | null;
  ccy?: VoteCcy;
  address?: string;
  seed?: string;
  accountReclaimPayout?: IAccountReclaimPayout | null;
  voteManager?: IVoteManager | null;
  accountReclaimRequests?: IAccountReclaimRequest[] | null;
}

export class ManagedAccount implements IManagedAccount {
  constructor(
    public id?: number,
    public accountName?: string | null,
    public ccy?: VoteCcy,
    public address?: string,
    public seed?: string,
    public accountReclaimPayout?: IAccountReclaimPayout | null,
    public voteManager?: IVoteManager | null,
    public accountReclaimRequests?: IAccountReclaimRequest[] | null
  ) {}
}
