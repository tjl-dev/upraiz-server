import { IVoteManagerPreferences } from '@/shared/model/vote-manager-preferences.model';
import { IManagedAccount } from '@/shared/model/managed-account.model';
import { IVoteTarget } from '@/shared/model/vote-target.model';
import { IAccountReclaimRequest } from '@/shared/model/account-reclaim-request.model';
import { IAccountReclaimPayout } from '@/shared/model/account-reclaim-payout.model';

export interface IVoteManager {
  id?: number;
  email?: string;
  name?: string;
  created?: Date | null;
  active?: boolean | null;
  comment?: string | null;
  voteManagerPreferences?: IVoteManagerPreferences | null;
  managedAccounts?: IManagedAccount[] | null;
  voteTargets?: IVoteTarget[] | null;
  accountReclaimRequests?: IAccountReclaimRequest[] | null;
  accountReclaimPayouts?: IAccountReclaimPayout[] | null;
}

export class VoteManager implements IVoteManager {
  constructor(
    public id?: number,
    public email?: string,
    public name?: string,
    public created?: Date | null,
    public active?: boolean | null,
    public comment?: string | null,
    public voteManagerPreferences?: IVoteManagerPreferences | null,
    public managedAccounts?: IManagedAccount[] | null,
    public voteTargets?: IVoteTarget[] | null,
    public accountReclaimRequests?: IAccountReclaimRequest[] | null,
    public accountReclaimPayouts?: IAccountReclaimPayout[] | null
  ) {
    this.active = this.active ?? false;
  }
}
