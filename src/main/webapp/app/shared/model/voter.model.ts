import { IVoterPreferences } from '@/shared/model/voter-preferences.model';
import { IVoterAccount } from '@/shared/model/voter-account.model';
import { IVote } from '@/shared/model/vote.model';

export interface IVoter {
  id?: number;
  email?: string;
  name?: string;
  created?: Date | null;
  active?: boolean | null;
  comment?: string | null;
  voterPreferences?: IVoterPreferences | null;
  voterAccounts?: IVoterAccount[] | null;
  votes?: IVote[] | null;
}

export class Voter implements IVoter {
  constructor(
    public id?: number,
    public email?: string,
    public name?: string,
    public created?: Date | null,
    public active?: boolean | null,
    public comment?: string | null,
    public voterPreferences?: IVoterPreferences | null,
    public voterAccounts?: IVoterAccount[] | null,
    public votes?: IVote[] | null
  ) {
    this.active = this.active ?? false;
  }
}
